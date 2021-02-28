<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentProducts" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Product List">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/popup.css">
  <p>
    Welcome to Expert-Soft training
  </p>
  <p>${cart}</p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sort="description" order="asc" arrow="&uarr;"/>
          <tags:sortLink sort="description" order="desc" arrow="&darr;"/>
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc" arrow="&uarr;"/>
          <tags:sortLink sort="price" order="desc" arrow="&darr;"/>
        </td>
        <td>
          Quantity
        </td>
        <td>
          Add to cart
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src=${product.imageUrl}>
        </td>
        <td>
          <a href="${pageContext.request.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td class="price">
          <a href="#popup${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
          <div id="popup${product.id}" class="overlay">
             <tags:priceHistory product="${product}"/>
          </div>
        </td>
        <td>
          <form id="addBtn${product.id}" method="post">
              <label>
                  <input name="quantity" class="quantity"
                         value="${not empty errors[product.id] ? param.quantity : 1}">
                  <input name="productId" type="hidden"  value="${product.id}">
              </label>
          <c:if test="${not empty errors[product.id]}">
            <div class="error">
                ${errors[product.id]}
            </div>
          </c:if>
          </form>
        </td>
        <td>
          <button form="addBtn${product.id}"
                  formaction="${pageContext.request.contextPath}/products/addToCart/${product.id}?backPath=products">
            Add to cart
          </button>
        </td>
      </tr>
    </c:forEach>
  </table>
  <c:if test="${not empty recentProducts}">
    <tags:recentProducts recentProducts="${recentProducts}"/>
  </c:if>
</tags:master>