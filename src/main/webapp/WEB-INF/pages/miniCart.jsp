<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
Cart: ${cart.totalQuantity} ${cart.totalQuantity==1 ? 'item':'items'}