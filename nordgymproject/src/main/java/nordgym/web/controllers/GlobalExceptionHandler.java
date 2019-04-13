package nordgym.web.controllers;

import nordgym.GlobalConstants;
import nordgym.error.EmptyDataBaseException;
import nordgym.error.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {
//    @ExceptionHandler(Throwable.class)
//    public ModelAndView handleGlobalErrors(Throwable e) {
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("message", GlobalConstants.SORRY_SOMETHING_WENT_WRONG);
//        return modelAndView;
//    }
    @ExceptionHandler({ResourceNotFoundException.class})
    public ModelAndView handleDBException(ResourceNotFoundException ex){
        return getModelAndView(ex.getMessage(), ex.getStatusCode());
    }

    @ExceptionHandler({EmptyDataBaseException.class})
    public ModelAndView handleDBException(EmptyDataBaseException ex){
        return getModelAndView(ex.getMessage(), ex.getStatusCode());
    }

    private ModelAndView getModelAndView(String message, int statusCode) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", message);
        modelAndView.addObject("status", String.format(GlobalConstants.ERROR, statusCode));
        return modelAndView;
    }

}
