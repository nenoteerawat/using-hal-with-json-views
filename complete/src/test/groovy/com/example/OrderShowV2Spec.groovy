package com.example

import grails.plugin.json.view.test.JsonViewTest
import grails.test.hibernate.HibernateSpec
import org.skyscreamer.jsonassert.JSONAssert

class OrderShowV2Spec extends HibernateSpec implements JsonViewTest {

    def "test _links appear in JSON"() {
        setup:
        def (Category clothing, Category furniture, Category tools) = BootStrap.fixtureCategories()
        def products = BootStrap.fixtureProducts(clothing, furniture, tools)
        def customers = BootStrap.fixtureCustomers()
        def orders  = BootStrap.fixtureOrders(products, customers)

        when:
        def order = orders.first()
        def result = render(view: "/order/show_v2", model:[order: order])
        def expectedJsonString = '''
        {
            _links: {
                self: {
                    href: "http://localhost:8080/api/orders/1",
                    hreflang: "en",
                    type: "application/hal+json"
                },
                customer: {
                    href: "http://localhost:8080/api/customers/1",
                    hreflang: "en",
                    type: "application/hal+json"
                }
            },
            id: "0A12321",
            shippingCost: 13.54,
            date: "2-08-2017",
            shippingAddress: {
                street: "321 Arrow Ln",
                street2: null,
                city: "Chicago",
                state: "IL",
                zip: 646465
            },
            products: [
                    {
                        id: 11
                    },
                    {
                        id: 1
                    },
                    {
                        id: 6
                    }
            ],
            customer: {
                id: 1
            }
        }
        '''
        JSONAssert.assertEquals(expectedJsonString, result.jsonText, false)

        then:
        notThrown AssertionError
    }
}
