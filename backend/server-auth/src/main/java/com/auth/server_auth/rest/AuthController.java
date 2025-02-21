package com.auth.server_auth.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.auth.server_auth.models.AuthResponse;
import com.auth.server_auth.models.UserCredentials;
import com.auth.server_auth.rmi.AuthService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Path("/auth")
public class AuthController {
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserCredentials credentials) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AuthService authService = (AuthService) registry.lookup("AuthService");
            boolean success = authService.login(credentials.getUsername(), credentials.getPassword());
            return Response.ok(new AuthResponse(success)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Server Error").build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserCredentials credentials) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AuthService authService = (AuthService) registry.lookup("AuthService");
            boolean success = authService.register(credentials.getUsername(), credentials.getPassword());
            return Response.ok(new AuthResponse(success)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Server Error").build();
        }
    }
}

