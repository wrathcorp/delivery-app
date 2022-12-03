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

    public List<DeliveryMetadata> getDeliveryMetadata() {

        TypedQuery<DeliveryMetadataEntity> query = em.createNamedQuery(
                "DeliveryMetadataEntity.getAll", DeliveryMetadataEntity.class);

        List<DeliveryMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(DeliveryMetadataConverter::toDto).collect(Collectors.toList());

    }

    public List<DeliveryMetadata> getDeliveryMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, DeliveryMetadataEntity.class, queryParameters).stream()
                .map(DeliveryMetadataConverter::toDto).collect(Collectors.toList());
    }

    public DeliveryMetadata getDeliveryMetadata(Integer id) {

        DeliveryMetadataEntity deliveryMetadataEntity = em.find(DeliveryMetadataEntity.class, id);

        if (deliveryMetadataEntity == null) {
            throw new NotFoundException();
        }

        return DeliveryMetadataConverter.toDto(deliveryMetadataEntity);
    }

    public DeliveryMetadata createDeliveryMetadata(DeliveryMetadata imageMetadata) {

        DeliveryMetadataEntity deliveryMetadataEntity = DeliveryMetadataConverter.toEntity(imageMetadata);

        try {
            beginTx();
            em.persist(deliveryMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (deliveryMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return DeliveryMetadataConverter.toDto(deliveryMetadataEntity);
    }

    public DeliveryMetadata putDeliveryMetadata(Integer id, DeliveryMetadata deliveryMetadata) {

        DeliveryMetadataEntity c = em.find(DeliveryMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        DeliveryMetadataEntity updatedDeliveryMetadataEntity = DeliveryMetadataConverter.toEntity(deliveryMetadata);

        try {
            beginTx();
            updatedDeliveryMetadataEntity.setId(c.getId());
            updatedDeliveryMetadataEntity = em.merge(updatedDeliveryMetadataEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return DeliveryMetadataConverter.toDto(updatedDeliveryMetadataEntity);
    }

    public boolean deleteDeliveryMetadata(Integer id) {

        DeliveryMetadataEntity deliveryMetadata = em.find(DeliveryMetadataEntity.class, id);

        if (deliveryMetadata != null) {
            try {
                beginTx();
                em.remove(deliveryMetadata);
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
