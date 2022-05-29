package ch.aaap.ca.be.medicalsupplies.model;

import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;

import java.util.Objects;

public class Producer {
    private String producerId;
    private String producerName;
    private String producerAddress;

    public Producer(MSProductRow msProductRow) {
        this.producerId = msProductRow.getProducerId();
        this.producerName = msProductRow.getProducerName();
        this.producerAddress = msProductRow.getProducerAddress();
    }

    public String getProducerId() {
        return producerId;
    }

    public String getProducerName() {
        return producerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producer producer = (Producer) o;
        return producerId.equals(producer.producerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producerId);
    }
}
