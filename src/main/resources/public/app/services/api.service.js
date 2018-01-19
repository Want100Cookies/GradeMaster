/**
 * HTTP API for GradeMaster
 * 
 * Simplify http requests
 * 
 * Made by Cas van Dinter
 */
app.factory('API', function ($cookies, $http, $httpParamSerializer) {
    /**
     * CONFIG
     */
    const BASE_URL = `http://localhost:8080`;
    const API = `api/v1`;
    const OAUTH_KEY = `Z3JhZGVtYXN0ZXItY2xpZW50OmdyYWRlbWFzdGVyLXNlY3JldA==`;
    const DEBUG = true;

    /**
     * DEFAULT REQUESTS
     */
    const REQUEST = {
        url: `${BASE_URL}/${API}/`,
        headers: {
            'Authorization': `Bearer ${$cookies.get("access_token")}`
        }
    };
    const AUTH_REQUEST = {
        url: `${BASE_URL}/oauth/token`,
        headers: {
            'Authorization': `Basic ${OAUTH_KEY}`,
            'Content-type': `application/x-www-form-urlencoded; charset=utf-8`
        },
    };

    /**
     * Auth user
     * @param {*} user object with username and password
     * @returns {Response}
     */
    this.auth = ({
        user
    } = {}) => {
        user = { ...user,
            grant_type: 'password',
            scope: 'read write'
        }
        let req = Object.assign({}, AUTH_REQUEST);
        return this.post({
            data: $httpParamSerializer(user),
            req
        }).then((resp) => {
            $cookies.put("access_token", resp.data.access_token);
            return resp;
        });
    };

    /**
     * GET request
     * @param {*} path path of request
     * @param {*} req (optional) request
     * @returns {Response}
     */
    this.get = ({
        path = ``,
        req = Object.assign({}, REQUEST)
    }) => {
        const METHOD = `GET`;
        req.method = METHOD;
        req.url = `${req.url}${path}`;
        return this.request(req);
    };

    /**
     * POST request
     * @param {*} path path of request 
     * @param {*} data data to be send
     * @param {*} req (optional) request
     * @returns {Response}
     */
    this.post = ({
        path = ``,
        data = {},
        req = Object.assign({}, REQUEST)
    }) => {
        const METHOD = `POST`;
        req.method = METHOD;
        req.url = `${req.url}${path}`;
        req.data = data;
        return this.request(req);
    };

    /**
     * PATCH request
     * @param {*} path path of request 
     * @param {*} data data to be send
     * @param {*} req (optional) request
     * @returns {Response}
     */
    this.patch = ({
        path = ``,
        data = {},
        req = Object.assign({}, REQUEST)
    }) => {
        const METHOD = `PATCH`;
        req.method = METHOD;
        req.url = `${req.url}${path}`;
        req.data = data;
        return this.request(req);
    };

    /**
     * DELETE request
     * @param {*} path path of request 
     * @param {*} data data to be send
     * @param {*} req (optional) request
     * @returns {Response}
     */
    this.delete = ({
        path = ``,
        data = {},
        req = Object.assign({}, REQUEST)
    }) => {
        const METHOD = `PATCH`;
        req.method = METHOD;
        req.url = `${req.url}${path}`;
        req.data = data;
        return this.request(req);
    };

    /**
     * Execute request
     * @param {*} req request
     * @returns {Response}
     */
    this.request = (req) => {
        if (DEBUG) {
            return $http(req).then((resp) => {
                console.log(resp);
                return resp;
            }).catch((error) => {
                console.error(error);
                return error;
            });
        }
        return $http(req);
    };

    /**
     * @returns {API}
     */
    return this;
});