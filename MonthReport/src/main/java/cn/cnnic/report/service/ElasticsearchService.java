package cn.cnnic.report.service;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class ElasticsearchService {
	private static RestHighLevelClient client;
	@Value(value = "${es.hosts}")
	private String hosts;
	
	public SearchResponse doSearch(String indexPattern,BoolQueryBuilder queryBuilder,
			ValuesSourceAggregationBuilder aggregation) throws IOException {
		init();
		SearchRequest searchRequest = new SearchRequest(indexPattern);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		if(null!=queryBuilder){
			searchSourceBuilder.query(queryBuilder);
		}
		if(null!=aggregation) {
			searchSourceBuilder.aggregation(aggregation);
		}
		searchRequest.source(searchSourceBuilder);
		return client.search(searchRequest);
	}

	private void init() {
		if(null!=client) {
			return;
		}
		System.out.println("es.hosts:"+hosts);
		RestClientBuilder clientBuilder=null;
		if(null==hosts||hosts.split(",").length==0) {
			clientBuilder=RestClient.builder(new HttpHost("localhost", 9200, "http"));
		}else {
			String []hostsArr=hosts.split(",");
			HttpHost[] httpHosts=new HttpHost[hostsArr.length];
			for(int i=0;i<hostsArr.length;i++) {
				String host=hostsArr[i];
				String protocol=host.split("://").length>1?host.split("://")[0]:"http";
				String ip=host.split("://").length>1?host.split("://")[1].split(":")[0]:host.split(":")[0];
				int port=(host.split("://").length>1&&host.split("://")[1].split(":").length>1)?Integer.valueOf(host.split("://")[1].split(":")[1]):9200;
				httpHosts[i]=new HttpHost(ip,port,protocol);
			}
			clientBuilder=RestClient.builder(httpHosts).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
	            @Override
	            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
	                requestConfigBuilder.setConnectTimeout(60*60*1000);
	                requestConfigBuilder.setSocketTimeout(60*60*1000);
	                requestConfigBuilder.setConnectionRequestTimeout(60*60*1000);
	                return requestConfigBuilder;
	            }
	        }).setMaxRetryTimeoutMillis(60*60*1000);
		}
		client = new RestHighLevelClient(clientBuilder);		
	}
}
