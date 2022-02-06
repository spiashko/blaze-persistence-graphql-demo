package com.spiashko.blazepersistencegraphqldemo;

import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupport;
import com.blazebit.persistence.integration.graphql.GraphQLEntityViewSupportFactory;
import com.blazebit.persistence.integration.graphql.GraphQLRelayConnection;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.Sorters;
import com.google.common.io.Resources;
import com.spiashko.blazepersistencegraphqldemo.repo.CatViewRepository;
import com.spiashko.blazepersistencegraphqldemo.repo.PersonViewRepository;
import com.spiashko.blazepersistencegraphqldemo.view.CatView;
import com.spiashko.blazepersistencegraphqldemo.view.PersonView;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


@RequiredArgsConstructor
@Component
public class GraphQLProvider {

    private final EntityViewManager evm;
    private final CatViewRepository catViewRepository;
    private final PersonViewRepository personViewRepository;

    private GraphQLEntityViewSupport graphQLEntityViewSupport;
    private GraphQLSchema schema;
    private GraphQL graphQL;

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, StandardCharsets.UTF_8);
        this.schema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        GraphQLEntityViewSupportFactory graphQLEntityViewSupportFactory = new GraphQLEntityViewSupportFactory(true, true);
        graphQLEntityViewSupportFactory.setImplementRelayNode(false);
        graphQLEntityViewSupportFactory.setDefineRelayNodeIfNotExist(true);
        this.graphQLEntityViewSupport = graphQLEntityViewSupportFactory.create(typeRegistry, evm);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("catById", new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
                                return catViewRepository.findById(graphQLEntityViewSupport.createSetting(dataFetchingEnvironment), Long.valueOf(dataFetchingEnvironment.getArgument("id")));
                            }
                        })
                        .dataFetcher("findAll", new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
                                EntityViewSetting<CatView, ?> setting = graphQLEntityViewSupport.createPaginatedSetting(dataFetchingEnvironment);
                                setting.addAttributeSorter("id", Sorters.ascending());
                                if (setting.getMaxResults() == 0) {
                                    return new GraphQLRelayConnection<>(Collections.emptyList());
                                }
                                return new GraphQLRelayConnection<>(catViewRepository.findAll(setting));
                            }
                        })
                        .dataFetcher("personFindAll", new DataFetcher() {
                            @Override
                            public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
                                EntityViewSetting<PersonView, ?> setting = graphQLEntityViewSupport.createPaginatedSetting(dataFetchingEnvironment);
                                setting.addAttributeSorter("id", Sorters.ascending());
                                if (setting.getMaxResults() == 0) {
                                    return new GraphQLRelayConnection<>(Collections.emptyList());
                                }
                                return new GraphQLRelayConnection<>(personViewRepository.findAll(setting));
                            }
                        })
                )
                .build();
    }

    @Bean
    public GraphQLSchema getSchema() {
        return schema;
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }
}
