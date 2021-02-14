<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentProducts" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/popup.css">
  <p>
    Welcome to Expert-Soft training
  </p>
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
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
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
      </tr>
    </c:forEach>
  </table>
  <tags:recentProducts recentProducts="${recentProducts}"/>
</tags:master>