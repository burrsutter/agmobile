package com.burrsutter.agmobile.rest.dto;

import java.io.Serializable;
import com.burrsutter.agmobile.model.Member;
import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MemberDTO implements Serializable
{

   private Long id;
   private String phoneNumber;
   private String email;
   private String name;

   public MemberDTO()
   {
   }

   public MemberDTO(final Member entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.phoneNumber = entity.getPhoneNumber();
         this.email = entity.getEmail();
         this.name = entity.getName();
      }
   }

   public Member fromDTO(Member entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Member();
      }
      entity.setPhoneNumber(this.phoneNumber);
      entity.setEmail(this.email);
      entity.setName(this.name);
      entity = em.merge(entity);
      return entity;
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public String getPhoneNumber()
   {
      return this.phoneNumber;
   }

   public void setPhoneNumber(final String phoneNumber)
   {
      this.phoneNumber = phoneNumber;
   }

   public String getEmail()
   {
      return this.email;
   }

   public void setEmail(final String email)
   {
      this.email = email;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }
}