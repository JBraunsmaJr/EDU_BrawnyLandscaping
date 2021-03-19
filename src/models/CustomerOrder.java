package models;

import java.util.Date;
import orm.annotations.*;
import orm.models.DbType;

public class CustomerOrder
{
    @Id
    private int id;

    @Required
    @DatabaseType(type = DbType.DATE)
    private Date orderedOnDate;

    @DatabaseType(type = DbType.DATE)
    private Date nextScheduledService;

    @ForeignKey(referenceClass = Address.class, backreferenceVariableName = "address")
    private int addressId;

    @NotMapped
    private Address address;

    @NotMapped
    private ServiceInterval serviceInterval;

    public void setId(int value) { id = value; }
    public void setOrderedOnDate(Date value) { orderedOnDate = value; }
    public void setNextScheduledService(Date value) { nextScheduledService = value; }
    public void setAddressId(int value) { addressId = value; }
    public void setAddress(Address value) { address = value; }
    public void setServiceInterval(ServiceInterval value) { serviceInterval = value; }

    public int getId() { return id; }
    public Date getOrderedOnDate() { return orderedOnDate; }
    public Date getNextScheduledService() { return nextScheduledService; }
    public int getAddressId() { return addressId; }
    public Address getAddress() { return address; }
    public ServiceInterval getServiceInterval() { return serviceInterval; }

}
