package com.wdy.cheemate.common.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestBean {
    private List<PageRequestBean.PageRequestItem> items = new ArrayList<>();
}
