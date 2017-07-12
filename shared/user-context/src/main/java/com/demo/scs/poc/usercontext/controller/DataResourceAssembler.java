package com.demo.scs.poc.usercontext.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.stereotype.Service;

import com.demo.scs.poc.usercontext.api.DataResource;
import com.demo.scs.poc.usercontext.api.NewData;
import com.demo.scs.poc.usercontext.domain.Data;

@Service
public class DataResourceAssembler extends EmbeddableResourceAssemblerSupport<Data, DataResource, UserContextController>{


   @Autowired
   public DataResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
      super(entityLinks, relProvider, UserContextController.class, DataResource.class);
   }

   @Override
   public Link linkToSingleResource(Data data) {
       return entityLinks.linkToSingleResource(DataResource.class, data.getUserId());
   }


   @Override
   public DataResource toResource(Data entity) {
      final DataResource resource = createResourceWithId(entity.getUserId(), entity);
      return resource;
   }

   @Override
   protected DataResource instantiateResource(Data entity) {
      return new DataResource(
               entity.getUserId(),
               entity.getData());
   }

   public Data toEntity(NewData newData) {
       return new Data()
               .setUserId(newData.getUserId())
               .setData(newData.getData());
   }
}

