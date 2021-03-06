/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.calderon.javawebstarter.projects.service;

import com.calderon.javawebstarter.projects.model.Users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 *
 * @author francisco
 */
@Stateless
@Path("users")
public class UsersFacadeREST extends AbstractFacade<Users> {
    @PersistenceContext(unitName = "javawebstarterpu")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Users createUser(Users entity) {
        if(entity.getPassword()!=null)
            entity.setPassword(hashSHA256(entity.getPassword()));

        return super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    public void edit(@PathParam("id") Integer id, Users entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Users find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/json"})
    public List<Users> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/json"})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private String hashSHA256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes("UTF-8"));
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte aDigest : digest) {
                String hex = Integer.toHexString(0xff & aDigest);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
