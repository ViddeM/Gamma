import { getRequest } from "../utils/api";
import { ADMIN_CLIENTS_ENDPOINT } from "../utils/endpoints";

export function getClients() {
    return getRequest(ADMIN_CLIENTS_ENDPOINT, input =>
        input.data.map(client => ({
            ...client
        }))
    );
}

export function getClient(clientUid) {
    return getRequest(ADMIN_CLIENTS_ENDPOINT + clientUid, input => ({
        data: {
            ...input.data
        }
    }));
}
