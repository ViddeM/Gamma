function getBackendUrl() {
    var backendUrl = process.env.REACT_APP_BACKEND_URL;
    return backendUrl == null ? "http://localhost:8081/api" : backendUrl;
}

export { getBackendUrl };
