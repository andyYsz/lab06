package com.something.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.something.auth.JwtTokenProvider;
import com.something.pojo.UserVo;
import com.something.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * UserController
 */
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * userService
     */
    @Autowired
    private UserService userService;
    /**
     * defaultKaptcha
     */
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    /**
     * jwtTokenProvider
     */
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    /**
     * authenticationManager
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * passwordEncoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * logout
     * @param request
     * @return
     */
    @PostMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("userVo");
        return "redirect:login";
    }

    /**
     * to index
     * @return
     */
    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    /**
     * register
     * @param userVo
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/register")
    public String register(@ModelAttribute UserVo userVo, Model model, HttpServletRequest request) {
        if(null != userVo && StringUtils.isEmpty(userVo.getUsername())){
            logger.error("username is empty");
            model.addAttribute("msg","username is empty");
            return "register";
        }
        if(null != userVo && StringUtils.isEmpty(userVo.getEmail())){
            logger.error("email is empty");
            model.addAttribute("msg","email is empty");
            return "register";
        }
        if(null != userVo && StringUtils.isEmpty(userVo.getName())){
            logger.error("name is empty");
            model.addAttribute("msg","name is empty");
            return "register";
        }
        if(null != userVo && StringUtils.isEmpty(userVo.getPassword())){
            logger.error("password is empty");
            model.addAttribute("msg","password is empty");
            return "register";
        }
        String s = request.getSession().getAttribute("CHECK_CODE").toString();
        if (null != userVo && StringUtils.isEmpty(userVo.getCaptchaCode()) || !s.equals(userVo.getCaptchaCode())) {
            logger.error("captcha code is incorrect");
            model.addAttribute("msg","captcha code is incorrect");
            return "register";
        }

        UserVo existUser = userService.getUserByUsername(userVo.getUsername());
        if(existUser != null){
            logger.error("username is exist");
            model.addAttribute("msg","username is exist");
            return "register";
        }
        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userService.register(userVo);
        logger.info("register successful");
        model.addAttribute("msg","register successful");
        return "login";
    }

    /**
     * to register
     * @param model
     * @return
     */
    @GetMapping(value = "/register")
    public String toRegister(Model model) {
        model.addAttribute("userVo",new UserVo());
        return "register";
    }

    /**
     * to modify
     * @param usernamme
     * @param model
     * @return
     */
    @GetMapping(value = "/modify")
    public String toModify(String usernamme, Model model) {
        UserVo userVoResult = userService.getUserByUsername(usernamme);
        model.addAttribute("userVo", userVoResult);
        return "modify";
    }

    /**
     * modify
     * @param userVo
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/modify")
    public String modify(@ModelAttribute UserVo userVo, Model model,HttpServletRequest request) {
        if(StringUtils.isEmpty(userVo.getEmail())){
            logger.error("email is empty");
            model.addAttribute("msg","email is empty");
            return "modify";
        }
        if(StringUtils.isEmpty(userVo.getName())){
            logger.error("name is empty");
            model.addAttribute("msg","name is empty");
            return "modify";
        }
        if(StringUtils.isEmpty(userVo.getNewPassword())){
            logger.error("new password is empty");
            model.addAttribute("msg","new password is empty");
            return "modify";
        }

        String s = request.getSession().getAttribute("CHECK_CODE").toString();
        if (StringUtils.isEmpty(userVo.getCaptchaCode()) || !s.equals(userVo.getCaptchaCode())) {
            logger.error("captcha code is incorrect");
            model.addAttribute("msg","captchaCode is incorrect");
            return "modify";
        }

        UserVo existUser = userService.getUserByUsername(userVo.getUsername());
        existUser.setName(userVo.getName());
        existUser.setEmail(userVo.getEmail());
        existUser.setUsername(userVo.getUsername());
        existUser.setPassword(userVo.getNewPassword());

        userService.update(existUser);
        logger.error("modify successful");
        model.addAttribute("msg","modify successful");
        return "modify";
    }

    /**
     * to login
     * @return
     */
    @GetMapping(value = "/login")
    public String toLogin() {
        return "login";
    }

    /**
     * login
     * @param userVo
     * @param model
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    public String login(@ModelAttribute UserVo userVo, Model model, HttpServletRequest request) {
        if(null != userVo && StringUtils.isEmpty(userVo.getUsername())){
            logger.error("username is empty");
            model.addAttribute("msg","username is empty");
            return "login";
        }
        if(null != userVo && StringUtils.isEmpty(userVo.getPassword())){
            logger.error("password is empty");
            model.addAttribute("msg","password is empty");
            return "login";
        }
        String s = request.getSession().getAttribute("CHECK_CODE").toString();
        if (null != userVo && StringUtils.isEmpty(userVo.getCaptchaCode()) || (null != s && !s.equals(userVo.getCaptchaCode()))) {
            logger.error("captcha code is incorrect");
            model.addAttribute("msg","captcha code is incorrect");
            return "login";
        }

        UserVo existUser = userService.getUserByUsername(userVo.getUsername());
        if(existUser != null){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(existUser.getUsername(), existUser.getPassword()));
            jwtTokenProvider.createToken(existUser.getUsername(), userService.getUserByUsername(existUser.getUsername()).getRoles());
            request.getSession().setAttribute("userVo", existUser);
            return "index";
        }else{
            model.addAttribute("msg","user not exist");
            return "login";
        }
    }

    /**
     * captcha
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/captcha")
    public void applyCheckCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        request.getSession().setAttribute("CHECK_CODE", text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }
}
