<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<a href="${pageContext.request.contextPath}/cart">Cart: ${cart.totalQuantity} ${cart.totalQuantity==1 ? 'item':'items'}</a>