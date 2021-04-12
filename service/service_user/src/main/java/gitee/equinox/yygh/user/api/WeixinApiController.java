package gitee.equinox.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import gitee.equinox.yygh.common.helper.JwtHelper;
import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.model.user.UserInfo;
import gitee.equinox.yygh.user.service.UserInfoService;
import gitee.equinox.yygh.user.utils.ConstantPropertiesUtil;
import gitee.equinox.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller//页面跳转，不使用rest controller返回数据
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;

    //1.生成微信二维码

    /**
     * 获取微信登录参数
     */
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            String redirectUri = URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
            map.put("redirectUri", redirectUri);
            map.put("state", System.currentTimeMillis() + "");//System.currentTimeMillis()+""
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //2.回调方法，得到扫码人信息
    @RequestMapping("callback")
    public String callback(String code, String state) {
        //获取授权临时票据
        System.out.println("code==" + code);
        //根据code值，请求微信提供的地址，得到返回的两个值，access_token和openid
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")//占位符%s
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        //使用工具类请求这个地址

        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo==>" + accessTokenInfo);
            /*
            code==051rKRkl26oKQ644xlol2Wa343di0rKRkf
            accessTokenInfo==>{"access_token":"44_BRBj7djSgKtOdQKr7dzu43K-BE9ykrtw3FheSIEa-YHldqdMBR-p11xDXx1cDYbf3ZHAcdJednWd1qyis4FbBJbFKUdkYlv96j1lgiGvjKXmo",
            "expires_in":7204330,
            "refresh_token":"44_M09UK_dvnEC3ad34WFPUmkyG7dktBkAy2qZRAzxFJdXgln94FtiHbVxQoZj78O3wxB6bqptk4Nihk3Yx2ce_YCUQydSqXa7l0Eba55TV-vjNw",
            "openid":"o3_SC50tiHq34433NiFFzDwdILk6KThc",
            "scope":"snsapi_login",
            "unionid":"oWgGz1JH_uCHH_Uz62h3434szBn-xPYA"}
             */

            //从返回的字符串，获取openid，和accesstoken
            JSONObject jsonObject = JSONObject.parseObject(accessTokenInfo);
            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            //根据openid判断，数据库中是否已经存在微信扫描人的信息
            UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
            if (userInfo == null) {
                //拿着两个值请求微信地址，得到扫描人的信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo===> " + resultInfo);

                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);

                //解析用户信息
                //昵称
                String nickname = resultUserInfoJson.getString("nickname");
                System.out.println("昵称" + nickname);
                //头像
                String headimgurl = resultUserInfoJson.getString("headimgurl");
                System.out.println("头像" + headimgurl);

                userInfo = new UserInfo();
                userInfo.setOpenid(openid);
                userInfo.setNickName(nickname);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }

            Map<String, Object> map = new HashMap<>();
            String name = userInfo.getName();
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if (StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);

            //手机号为空，返回openid
            //不为空，绑定手机号--前端判断
            if (StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }

            //jwt生成token字符串
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            //跳转前端页面
            return "redirect:" +
                    ConstantPropertiesUtil.YYGH_BASE_URL +
                    "/weixin/callback?token=" + map.get("token") +
                    "&openid=" + map.get("openid") + "&name=" +
                    URLEncoder.encode((String) map.get("name"),"utf-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

