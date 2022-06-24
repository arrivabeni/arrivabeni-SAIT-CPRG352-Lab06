package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ShoppingListServlet extends HttpServlet {

    private final String REGISTER_JSP = "/WEB-INF/register.jsp";
    private final String SHOPPING_LIST_JSP = "/WEB-INF/shoppingList.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the session
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        String action = request.getParameter("action");
        String destination = "";

        // Make the variables null safe
        username = (username == null) ? "" : username;
        action = (action == null) ? "" : action;

        switch (action) {
            case "logout":
                // Destroy the session
                session.invalidate();
                response.sendRedirect("ShoppingList");
                return;
            default:
                if (username.isEmpty()) {
                    destination = REGISTER_JSP;
                } else {
                    destination = SHOPPING_LIST_JSP;
                }
        }

        // Will never happens, but if the destination was not defined, define as Register page
        if (destination.isEmpty()) {
            destination = REGISTER_JSP;
        }

        // load the Register page
        getServletContext().getRequestDispatcher(destination).forward(request, response);
        return; // Stop code call after send it to a jsp
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String destination = "";
        // Get the session
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String username = request.getParameter("username");

        // Make the variables null safe
        username = (username == null) ? "" : username;
        action = (action == null) ? "" : action;

        switch (action) {
            case "register":
                if (username.isEmpty()) {
                    destination = REGISTER_JSP;
                } else {
                    session.setAttribute("username", username);
                    destination = SHOPPING_LIST_JSP;
                }
                break;
            case "add":

                addToCart(request, session);

                destination = SHOPPING_LIST_JSP;
                break;
            default:
                destination = REGISTER_JSP;
        }
        getServletContext().getRequestDispatcher(destination).forward(request, response);
        return; // Stop code call after send it to a jsp

    }

    private void addToCart(HttpServletRequest request, HttpSession session) {

        String productName = request.getParameter("name");
        productName = (productName == null) ? "" : productName;

        if (productName.isEmpty()) {
            return;
        }

        ArrayList<String> lstItems = (ArrayList<String>) session.getAttribute("items");
        lstItems = (lstItems == null) ? new ArrayList<>() : lstItems;

        lstItems.add(productName);

        session.setAttribute("items", lstItems);
    }

}
