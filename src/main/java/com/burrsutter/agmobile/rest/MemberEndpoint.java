package com.burrsutter.agmobile.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import com.burrsutter.agmobile.model.Member;
import com.burrsutter.agmobile.rest.dto.MemberDTO;

/**
 * 
 */
@Stateless
@Path("forge/members")
public class MemberEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(MemberDTO dto)
   {
      Member entity = dto.fromDTO(null, em);
      em.persist(entity);
      // return Response.created(UriBuilder.fromResource(MemberEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
      return Response.created(UriBuilder.fromResource(MemberEndpoint.class).path(String.valueOf(entity.getId())).build()).entity(entity).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Member entity = em.find(Member.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Member> findByIdQuery = em.createQuery("SELECT DISTINCT m FROM Member m WHERE m.id = :entityId ORDER BY m.id", Member.class);
      findByIdQuery.setParameter("entityId", id);
      Member entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      MemberDTO dto = new MemberDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<MemberDTO> listAll()
   {
      final List<Member> searchResults = em.createQuery("SELECT DISTINCT m FROM Member m ORDER BY m.id", Member.class).getResultList();
      final List<MemberDTO> results = new ArrayList<MemberDTO>();
      for (Member searchResult : searchResults)
      {
         MemberDTO dto = new MemberDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id, MemberDTO dto)
   {
      TypedQuery<Member> findByIdQuery = em.createQuery("SELECT DISTINCT m FROM Member m WHERE m.id = :entityId ORDER BY m.id", Member.class);
      findByIdQuery.setParameter("entityId", id);
      Member entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      entity = dto.fromDTO(entity, em);
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}