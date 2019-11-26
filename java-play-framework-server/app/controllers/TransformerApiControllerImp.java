package controllers;

import apimodels.ErrorMsg;
import apimodels.GeneInfo;
import apimodels.TransformerInfo;
import apimodels.TransformerQuery;

import filter.RemoveGenesFilter;

import play.mvc.Http;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileInputStream;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaPlayFrameworkCodegen", date = "2019-11-26T22:06:46.884Z")

public class TransformerApiControllerImp implements TransformerApiControllerImpInterface {
    @Override
    public List<GeneInfo> transformPost(TransformerQuery query) throws Exception {
        return RemoveGenesFilter.filter(query);
    }

    @Override
    public TransformerInfo transformerInfoGet() throws Exception {
        return RemoveGenesFilter.transformerInfo();
    }

}
