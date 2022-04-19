package com.alliance.claimsapp.Controller;

import com.alliance.claimsapp.model.Employee;
import com.alliance.claimsapp.service.EmployeeService;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/addEmployee")
    public ModelAndView registerEmployeeController(@ModelAttribute Employee employee){

        employee.setUserStatus("active");
        Employee savedEmployee = employeeService.registerEmployeeService(employee);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listOfEmployees" ,employeeService.getAllEmployeeService());
        modelAndView.setViewName("employeeList");

        return modelAndView;
    }

    @GetMapping("/login")
    public String indexPage(){
        return "login";
    }

    @PostMapping("/homepage")
    public ModelAndView loginEmployeeController(@ModelAttribute Employee employee, HttpSession httpSession){
        List<Employee> listOfEmployee= employeeService.getAllEmployeeService();
        ModelAndView modelAndView = new ModelAndView();
        Employee sessionEmployee = employeeService.loginEmployeeService(employee.getEmail(), employee.getPassword());
        modelAndView.addObject("employee", sessionEmployee);
        modelAndView.addObject("listOfEmployees", listOfEmployee);

        httpSession.setAttribute("Employee", sessionEmployee);

        if(sessionEmployee.getDepartment().contains("Accounting")){
            modelAndView.setViewName("employeeList");
        } else {
            modelAndView.setViewName("claimsList"); //THIS IS ANDREI'S CLAIMS FORM employeeClaimForm
        }
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logoutEmployeeController(HttpServletRequest request){
        HttpSession sessionEmployee = request.getSession(false);
        if (sessionEmployee != null) {
            sessionEmployee.invalidate();
        }
        return "login";
    }


    @PostMapping("/deleteEmployeeSession")
    public String deleteSessionEmployeeController(Long id){
        employeeService.deleteEmployeeService(id);
        return "login";
    }

    @PostMapping("/deleteEmployee")
    @ResponseBody
    public void deleteEmployeeController(Long id){
        employeeService.deleteEmployeeService(id);
    }


    @PostMapping("/editSessionEmployeePassword")
    @ResponseBody
    public Employee editSessionEmployeePasswordController(Long id, String password){
        return employeeService.editSessionEmployeePasswordService(id, password);
    }

    @PostMapping("/validateSessionEmployeePassword")
    @ResponseBody
    public String validateSessionEmployeePassword(Long id, String password){
        return employeeService.validateSessionEmployeePasswordService(id, password);
    }

    @PostMapping("/editSessionName")
    @ResponseBody
    public void editSessionNameController(Long id, String firstName, String lastName){
        employeeService.editSessionNameService(id, firstName, lastName);
    }

}
