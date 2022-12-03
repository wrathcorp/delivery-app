package rso.project.delivery.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import rso.project.delivery.lib.DeliveryMetadata;
import rso.project.delivery.models.converters.DeliveryMetadataConverter;
import rso.project.delivery.models.entities.DeliveryMetadataEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class DeliveryMetadataBean {

    private Logger log = Logger.getLogger(DeliveryMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    public List<DeliveryMetadata> getImageMetadata() {

        TypedQuery<DeliveryMetadataEntity> query = em.createNamedQuery(
                "DeliveryMetadataEntity.getAll", DeliveryMetadataEntity.class);

        List<DeliveryMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(DeliveryMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<DeliveryMetadata> getImageMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, DeliveryMetadataEntity.class, queryParameters).stream()
                .map(DeliveryMetadataConverter::toDto).collect(Collectors.toList());
    }

    public DeliveryMetadata getImageMetadata(Integer id) {

        DeliveryMetadataEntity imageMetadataEntity = em.find(DeliveryMetadataEntity.class, id);

        if (imageMetadataEntity == null) {
            throw new NotFoundException();
        }

        DeliveryMetadata deliveryMetadata = DeliveryMetadataConverter.toDto(imageMetadataEntity);

        return deliveryMetadata;
    }

    public DeliveryMetadata createImageMetadata(DeliveryMetadata imageMetadata) {

        DeliveryMetadataEntity imageMetadataEntity = DeliveryMetadataConverter.toEntity(imageMetadata);

        try {
            beginTx();
            em.persist(imageMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (imageMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return DeliveryMetadataConverter.toDto(imageMetadataEntity);
    }

    public DeliveryMetadata putImageMetadata(Integer id, DeliveryMetadata imageMetadata) {

        DeliveryMetadataEntity c = em.find(DeliveryMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        DeliveryMetadataEntity updatedImageMetadataEntity = DeliveryMetadataConverter.toEntity(imageMetadata);

        try {
            beginTx();
            updatedImageMetadataEntity.setId(c.getId());
            updatedImageMetadataEntity = em.merge(updatedImageMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return DeliveryMetadataConverter.toDto(updatedImageMetadataEntity);
    }

    public boolean deleteImageMetadata(Integer id) {

        DeliveryMetadataEntity imageMetadata = em.find(DeliveryMetadataEntity.class, id);

        if (imageMetadata != null) {
            try {
                beginTx();
                em.remove(imageMetadata);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
