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

    private final double PAGE_SIZE = 10.0; // Type double helps to round page number
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
                // Force cleaning of url parameters
                response.sendRedirect("ShoppingList");
                return;
            case "page_forward":
            case "page_backward":
                boolean fwd = action.equals("page_forward");
                paginate(session, fwd);
                // Force cleaning of url parameters
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
            case "delete":
                removeFromCart(request, session);
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

        ArrayList<String> lstItems = (ArrayList<String>) session.getAttribute("itemsTotal");

        // First item
        lstItems = lstItems == null ? new ArrayList<>() : lstItems;

        lstItems.add(productName);

        session.setAttribute("itemsTotal", lstItems);
        paginate(session);
    }

    private void removeFromCart(HttpServletRequest request, HttpSession session) {

        String selected = request.getParameter("selected");

        if (selected == null) {
            return;
        }

        ArrayList<String> lstItems = (ArrayList<String>) session.getAttribute("itemsTotal");
        lstItems.remove(selected);
        paginate(session);

        session.setAttribute("itemsTotal", lstItems);
    }

    private void paginate(HttpSession session) {
        ArrayList<String> lstItemsTotal = (ArrayList<String>) session.getAttribute("itemsTotal");
        ArrayList<String> lstItems = new ArrayList<>();
        Object actualPageObj = session.getAttribute("actualPage");
        int actualPage = actualPageObj == null ? 1 : (int) actualPageObj;
        int totalPages = (int) Math.ceil(lstItemsTotal.size() / PAGE_SIZE);

        //Prevent page to be out of bounds
        actualPage = (actualPage == 0) ? 1 : actualPage;
        actualPage = (actualPage > totalPages) ? totalPages : actualPage;

        int offset = (int) ((actualPage - 1) * PAGE_SIZE);
        int size = (int) (offset + PAGE_SIZE);

        for (int i = 0; i < lstItemsTotal.size(); i++) {
            String item = lstItemsTotal.get(i);

            if (i < offset) {
                continue;
            }
            if (i >= size) {
                break;
            }

            lstItems.add(item);
        }

        session.setAttribute("totalPages", totalPages);
        session.setAttribute("actualPage", actualPage);
        session.setAttribute("items", lstItems);
    }

    private void paginate(HttpSession session, boolean forward) {

        int actualPage = (int) session.getAttribute("actualPage");

        if (forward) {
            actualPage++;
        } else {
            actualPage--;
        }

        session.setAttribute("actualPage", actualPage);
        paginate(session);
    }

}
