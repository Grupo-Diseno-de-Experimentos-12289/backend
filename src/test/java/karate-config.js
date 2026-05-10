function fn() {
  var env = karate.env; // get java system property 'karate.env'
  if (!env) {
    env = 'dev'; // a custom default
  }
  var config = {
    baseUrl: 'http://localhost:8091/api/v1'
  };
  // don't waste time waiting for a connection or if servers don't respond within 5 seconds
  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  return config;
}

