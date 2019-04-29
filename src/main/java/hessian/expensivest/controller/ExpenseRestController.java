package hessian.expensivest.controller;

import hessian.expensivest.domain.Expense;
import hessian.expensivest.repository.ExpenseRepository;
import hessian.expensivest.repository.ExpenseSearchRepository;
import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExpenseRestController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSearchRepository expenseSearchRepository;

    private AnyParser anyParser = new AnyParser();

    @RequestMapping("api/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping(value = "api/add", method = RequestMethod.POST)
    public Expense createExpense(@RequestBody Expense expense) {
        expenseRepository.save(expense);
        expenseSearchRepository.save(expense);
        return expense;
    }

    @RequestMapping("api/")
    public List<Expense> all() {
        return expenseRepository.findAll();
    }

    @RequestMapping("api/user/{user}")
    public List<Expense> user(@PathVariable String user) throws ParseException {
        return expenseRepository.findByKeyUser(anyParser.parse(user, String.class));
    }

    @RequestMapping("api/user_trip/{user}/{trip}")
    public List<Expense> userTrip(@PathVariable String user, @PathVariable String trip) throws ParseException {
        return expenseRepository.findByKeyUserAndKeyTrip(anyParser.parse(user, String.class), anyParser.parse(trip, String.class));
    }

    @RequestMapping("api/category/{cat}")
    public List<Expense> category(@PathVariable String cat) throws ParseException {
        return expenseRepository.findByCategory(anyParser.parse(cat, String.class));
    }

    @RequestMapping("api/amount/gt/{amount}")
    public List<Expense> amount_gt(@PathVariable String amount) throws ParseException {
        return expenseRepository.findByAmountGreaterThan(anyParser.parse(amount, Double.class));
    }

}
