package nordgym.web.controllers;

import nordgym.GlobalConstants;
import nordgym.error.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleDatabaseErrors(Throwable e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", GlobalConstants.SORRY_SOMETHING_WENT_WRONG);
        return modelAndView;
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleDBException(UserNotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message",ex.getMessage());
        modelAndView.addObject("status", String.format(GlobalConstants.ERROR,ex.getStatusCode()));
        return modelAndView;
    }
}
