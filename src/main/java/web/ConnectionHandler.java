package web;

import web.template.TemplateEngine;
import web.util.HttpAnswerCode;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConnectionHandler implements  Runnable{

    private Socket socket;
    private WebServerConfig config;

    private TemplateEngine templateEngine = new TemplateEngine();

    public ConnectionHandler(Socket socket, WebServerConfig config) {
        this.socket = socket;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int readIndex = 0;
            String startLine = null;
            String line = null;
            while((line = bufferedReader.readLine()) != null){

                if(readIndex == 0){
                    startLine = line;
                }
                readIndex++;

                if(line.length() == 0){
                    break;
                }

            }
            sendAnswer(socket, startLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswer(Socket socket, String startLine) throws IOException {
        String filePath = getFilePathFromStartLine(startLine);

        HttpResponse response = new HttpResponse();
        if(fileExists(filePath)){

            if(isAllowed(filePath, startLine)){
                response.setCode(HttpAnswerCode.CODE_200);

                String fileContent = readFile(filePath);
                fileContent = templateEngine.process(fileContent);

                response.setContent(fileContent);
            }else {
                response.setCode(HttpAnswerCode.CODE_503);
                response.setContent("У вас нет прав доступа к этой странице");
            }

        }else{
            response.setCode(HttpAnswerCode.CODE_404);
            response.setContent("Страница не найдена");
        }

        socket.getOutputStream().write(response.toString().getBytes());
        socket.getOutputStream().flush();
    }

    private boolean isAllowed(String filePath, String startLine){
        if(filePath.startsWith(config.webFolder + config.adminPath)){

            Map<String, String> params = parseGetParameters(startLine);
            if("admin".equals(params.get("name")) &&
                    "password".equals(params.get("password"))){
                return true;
            }

            return false;
        }

        return true;
    }

    private String getFilePathFromStartLine(String startLine){
        String[] parts = startLine.split(" ");
        String uriPart = parts[1];

        String[] uriParts = uriPart.split("\\?");
        uriPart = uriParts[0];

        if(uriPart.equals("/")){
            uriPart = "/" + config.defaultPage;
        }

        return config.webFolder + uriPart;
    }

    private String readFile(String pathToFile){
        StringBuilder result = new StringBuilder();

        try {
            Scanner sc = new Scanner(new File(pathToFile));
             while (sc.hasNextLine()){
                 if(result.length() > 0){
                     result.append("\n");
                 }
                 result.append(sc.nextLine());
             }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private boolean fileExists(String pathToFile){
        return new File(pathToFile).exists();
    }

    private Map<String, String> parseGetParameters(String startLine){
        Map<String, String> result = new HashMap<>();
        String[] parts = startLine.split(" ");
        String uriPart = parts[1];

        String[] uriParts = uriPart.split("\\?");

        if(uriParts.length > 1) {
            String params = uriParts[1];

            String[] keyValueParts = params.split("&");
            for(String kvPart: keyValueParts){
                String[] pair = kvPart.split("=");
                String key = pair[0];
                String value = pair[1];

                result.put(key, value);
            }
        }

        return result;
    }
}
