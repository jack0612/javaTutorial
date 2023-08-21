package com.pokemonreview.api.Advice;

 

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {

	@Before(value="execution(* com.pokemonreview.api.controllers.PokemonController.*(..))")
	public void beforeAdvice(JoinPoint joinPoint) {
		System.out.println("Request to"+ joinPoint.getSignature()+" started at "+ new Date());
	}
}
