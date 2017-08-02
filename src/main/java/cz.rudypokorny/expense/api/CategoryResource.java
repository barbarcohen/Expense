package cz.rudypokorny.expense.api;

import cz.rudypokorny.expense.service.IExpenseService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


//FIXME RPO" initialize also the error hanler mapper
@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CategoryResource {

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll(){
        return ResponseEntity.ok(expenseService.findAllCategories().get());
    }

}
