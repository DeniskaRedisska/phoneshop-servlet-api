<%@ tag trimDirectiveWhitespaces="true"%>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="arrow" required="true" %>

<a href="?sort=${sort}&order=${order}&query=${param.query}"
   style="${sort eq param.sort and order eq param.order ? 'color: #06D85F':''}">${arrow}</a>