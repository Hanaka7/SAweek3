package sa.server;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/senderapi/rest")
public class senderapicontroller implements sender {
    static final String From = "admin@hanaserver.top";

    private static final String accessKeyId;
    private static final String secret;
    /**
     * 初始化获取私钥
     * */
    static {
        ClassPathResource smsKey = new ClassPathResource("key/SMSKey.txt");
        System.out.println(smsKey);
        Scanner scanner = null;
        try {
            scanner = new Scanner(smsKey.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        accessKeyId = scanner.nextLine();
        secret = scanner.nextLine();
        scanner.close();
    }

    @RequestMapping(value = "/sendmail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public restres sendEmail(@RequestBody user user) {
        restres restresult = new restres();
        String Subject = "Test";
        String HtmlBody = "<h1>Mail sender</h1>" + "<p>" + user.getPayload() + "</p>";
        String TextBody = user.getPayload();
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FpL54YUN1DFQFhXyWcd","bHrv1Cg7ms1Fb9sZfCdDn4ByKGvXX7");
            IAcsClient client = new DefaultAcsClient();
            SingleSendMailRequest request = new SingleSendMailRequest();

            request.setAccountName(From);
            request.setFromAlias("admin");
            request.setAddressType(1);
            request.setReplyToAddress(false);
            request.setToAddress(user.getUrl());
            request.setSubject(Subject);
            request.setHtmlBody(HtmlBody);
            request.setTextBody(TextBody);

            SingleSendMailResponse response = client.getAcsResponse(request);
            System.out.println("email sent");
            restresult.setMsg("succeed");
        } catch (ServerException e) {
            System.out.println(e.getErrCode());
            e.printStackTrace();
            restresult.setMsg("fail");
        } catch (ClientException e) {
            System.out.println(e.getErrCode());
            e.printStackTrace();
            restresult.setMsg("fail");
        } catch (Exception e) {
            e.printStackTrace();
            restresult.setMsg("unknown error");
        }
        return restresult;
    }

    @RequestMapping(value = "/sendmailbatch", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public restres sendEmailBatch(@RequestBody batchofusers user){
        restres restresult = new restres();
        restresult.setMsg("succeed");
        String[] urls = user.getUrl();

        String Subject = "Test";
        String HtmlBody = "<h1>Mail sender</h1>" + "<p>" + user.getPayload() + "</p>";
        String TextBody = user.getPayload();

        IClientProfile profile;
        IAcsClient client;
        SingleSendMailRequest request;

        try {
            profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FpL54YUN1DFQFhXyWcd", "bHrv1Cg7ms1Fb9sZfCdDn4ByKGvXX7");
            client = new DefaultAcsClient();
            request = new SingleSendMailRequest();
            request.setAccountName(From);
            request.setFromAlias("admin");
            request.setAddressType(1);
            request.setReplyToAddress(false);
            request.setSubject(Subject);
            request.setHtmlBody(HtmlBody);
            request.setTextBody(TextBody);
        }catch (Exception e){
            restresult.setMsg("Unknown error");
            return restresult;
        }

        for(String url:urls){
            try {
                request.setToAddress(url);
                SingleSendMailResponse response = client.getAcsResponse(request);
            }catch (ServerException e){
                System.out.println(e.getCause());
                e.printStackTrace();
                restresult.setMsg("fail");
            }catch (ClientException e){
                System.out.println(e.getCause());
                e.printStackTrace();
                restresult.setMsg("fail");
            }
        }
        return restresult;
    }
    @RequestMapping(value ="/decidemail",method = RequestMethod.POST,produces="application/json")
    @ResponseBody
    public restres decidemailaddress(@RequestBody user user){
        restres restresult = new restres();
        String str="([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,6})$";
        if(user.getUrl()==null){
            restresult.setMsg("fail");
            return restresult;
        }
        boolean decide = Pattern.matches(str,user.getUrl());
        if(decide){
            restresult.setMsg("succeed");
        }
        else{
            restresult.setMsg("fail");
        }
        return restresult;
    }


}
