package sa.wsdemo;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Endpoint
public class wsendpoint {
    private static final String NAMESPACE_URI = "sa/wsdemo";
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
    static final String From = "admin@hanaserver.top";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deciderequest")
    @ResponsePayload
    public Userresponse decide(@RequestPayload Deciderequest validateRequest){
        Userresponse soapResult = new Userresponse();
        String pattern = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,6})$";
        if(validateRequest.getUrl() == null){
            soapResult.setMsg("fail");
        }
        boolean valid = Pattern.matches(pattern, validateRequest.getUrl());
        if(valid){
            soapResult.setMsg("succeed");
        }
        else {
            soapResult.setMsg("fail");
        }
        return soapResult;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "userrequest")
    @ResponsePayload
    public Userresponse sendEmail(@RequestPayload Userrequest userRequest){
        Userresponse soapResult = new Userresponse();
        String Subject = "Test";
        String HtmlBody = "<h1>Mail sender</h1>"
                + "<p>" + userRequest.getPayload() + "</p>";
        String TextBody = userRequest.getPayload();
        try{
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
            IAcsClient client = new DefaultAcsClient();
            SingleSendMailRequest request = new SingleSendMailRequest();

            request.setAccountName(From);
            request.setFromAlias("admin");
            request.setAddressType(1);
            request.setReplyToAddress(false);
            request.setToAddress(userRequest.getUrl());
            request.setSubject(Subject);
            request.setHtmlBody(HtmlBody);
            request.setTextBody(TextBody);

            SingleSendMailResponse response = client.getAcsResponse(request);
            System.out.println("email sent");
            soapResult.setMsg("succeed");
        }
        catch (ServerException e){
            System.out.println(e.getErrCode());
            e.printStackTrace();
            soapResult.setMsg("fail");
        }
        catch (ClientException e){
            System.out.println(e.getErrCode());
            e.printStackTrace();
            soapResult.setMsg("fail");
        }
        catch (Exception e){
            e.printStackTrace();
            soapResult.setMsg("Unknown error");
        }
        return soapResult;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "usersbatchrequest")
    @ResponsePayload
    public Userresponse sendEmailBatch(@RequestPayload Usersbatchrequest userRequest) {
        Userresponse soapResult = new Userresponse();
        soapResult.setMsg("succeed");
        List<String> urls = userRequest.getUrl();

        String Subject = "Test";
        String HtmlBody = "<h1>Mail sender/h1>"
                + "<p>" + userRequest.getPayload() + "</p>";
        String TextBody = userRequest.getPayload();

        IClientProfile profile;
        IAcsClient client;
        SingleSendMailRequest request;

        try {
            profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
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
            soapResult.setMsg("Unknown error");
            return soapResult;
        }

        for(String url:urls){
            try {
                request.setToAddress(url);
                SingleSendMailResponse response = client.getAcsResponse(request);
            }catch (ServerException e){
                System.out.println(e.getCause());
                e.printStackTrace();
                soapResult.setMsg("fail");
            }catch (ClientException e){
                System.out.println(e.getCause());
                e.printStackTrace();
                soapResult.setMsg("fail");
            }
        }

        return soapResult;
    }

}
