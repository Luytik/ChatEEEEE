package ua.kiev.prog;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "Users", value = "/users")
public class UsersServlet extends HttpServlet {

    UserList userList = UserList.getInstance();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String html = "";
        System.out.println("in post");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"))){
            String text = "";
            for(;;){
                text = br.readLine();

                if(text == null)
                    break;
                html += text;
            }
        }
        System.out.println(html);
        if(html.startsWith("-")){
            String[] req = html.split(" ");
            switch (req[0]){
                case "-changeStatus":
                    userList.changeStatus(req[1], req[2]);
                    break;
                case "-deleteAccount":
                    System.out.println("in case");
                    Account ac = new Account(req[1]);
                    userList.exitAccount(ac);
                    break;
            }
            return;
        }

        Gson gson = new GsonBuilder().create();
        Account account = gson.fromJson(html, Account.class);

        if(!userList.addUser(account)){
            response.sendError(502);
        }


    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException{
        response.setContentType("application/json");
        System.out.println("in get");

        PrintWriter pw = response.getWriter();
        Gson gson = new GsonBuilder().create();
        pw.write(gson.toJson(userList.toJson()));
    }
}
