function fn() {
    var env = karate.env;
    karate.log('karate.env system property was:', env);
    if (!env) {
        env = 'dev';
    }
    var config = {
        env: env,
        baseUrl: 'http://localhost:8091/api/v1'
    };
    if (env == 'dev') {
        config.baseUrl = 'http://localhost:8091/api/v1';
    } else if (env == 'e2e') {
        config.baseUrl = 'http://localhost:8091/api/v1';
    }
    return config;
}