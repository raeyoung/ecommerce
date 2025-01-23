package kr.hhplus.be.server.interfaces.order;

import java.util.List;


public record OrderRequest (
    List<OrderProduct> orderProductList
){
}