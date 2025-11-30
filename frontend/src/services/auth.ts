import api from './api';

export interface LoginCredentials {
    username: string;
    password: string;
}

export interface AuthResponse {
    message: string;
    username: string;
}

export interface AuthStatus {
    authenticated: boolean;
    username: string | null;
}

class AuthService {
    async login(credentials: LoginCredentials): Promise<AuthResponse> {
        // Store credentials for Basic Auth
        const basicAuth = btoa(`${credentials.username}:${credentials.password}`);
        localStorage.setItem('auth', basicAuth);

        // Set default auth header
        api.defaults.headers.common['Authorization'] = `Basic ${basicAuth}`;

        const response = await api.post<AuthResponse>('/auth/login', credentials);
        return response.data;
    }

    async logout(): Promise<void> {
        localStorage.removeItem('auth');
        delete api.defaults.headers.common['Authorization'];
        await api.post('/auth/logout');
    }

    async getStatus(): Promise<AuthStatus> {
        const response = await api.get<AuthStatus>('/auth/status');
        return response.data;
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem('auth');
    }

    initAuth(): void {
        const auth = localStorage.getItem('auth');
        if (auth) {
            api.defaults.headers.common['Authorization'] = `Basic ${auth}`;
        }
    }
}

export default new AuthService();