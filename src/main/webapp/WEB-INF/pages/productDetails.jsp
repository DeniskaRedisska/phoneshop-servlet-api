<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<jsp:useBean id="recentProducts" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <head>
        <title>Product Details</title>
    </head>
    <body>
    <h2><b>Product Details</b></h2>
    <h3><b>${product.description}</b></h3>
    <p>
        Cart: ${cart}
    </p>

    <c:choose>
        <c:when test="${not empty error}">
            <div class="error">
                An error occurred adding product to cart
            </div>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty param.message}">
                <div class="success">
                    ${param.message}
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>

    <form method="post"
          action="${pageContext.request.contextPath}/products/addToCart/${product.id}?backPath=products/${product.id}">
    <table>
        <tr>
            <td>Image</td>
            <td><img src="${product.imageUrl}"></td>
        </tr>
        <tr>
            <td>Code</td>
            <td>${product.code}</td>
        </tr>
        <tr>
            <td>Stock</td>
            <td>${product.stock}</td>
        </tr>
        <tr>
            <td>Price</td>
            <td>
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td>quantity</td>
            <td>
                <input name="quantity" class="quantity" value="${not empty errors ? param.quantity : 1}">
                <input name="productId" type="hidden" value="${product.id}">
                <c:if test="${not empty errors}">
                    <div class="error">
                        ${errors[product.id]}
                    </div>
                </c:if>
            </td>
        </tr>
    </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
    <c:if test="${not empty recentProducts}">
        <tags:recentProducts recentProducts="${recentProducts}"/>
    </c:if>
    </body>
</tags:master>

