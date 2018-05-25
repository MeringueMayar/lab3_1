/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage;


public class ClientDataBuilder {
    private Id id;
    private String name;

    public ClientDataBuilder() {
    }

    public ClientDataBuilder setId(Id id) {
        this.id = id;
        return this;
    }

    public ClientDataBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ClientData createClientData() {
        return new ClientData(id, name);
    }
    
}
