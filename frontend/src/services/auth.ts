import api from './api';

export interface LoginCredentials {
    username: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    username: string;
}

export interface AuthStatus {
    authenticated: boolean;
    username: string | null;
}

class authService {
    async login(credentials: LoginCredentials): Promise<LoginResponse> {
        const response = await api.post<LoginResponse>('/auth/login', credentials);

        localStorage.setItem('token', response.data.token);
        localStorage.setItem('username', response.data.username);

        api.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`;

        return response.data;
    }

    async logout(): Promise<void> {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        delete api.defaults.headers.common['Authorization'];

        try {
            await api.post('/auth/logout');
        } catch (error) {
            // Ignore errors on logout
            console.error('Logout error:', error);
        }
    }

    async getStatus(): Promise<AuthStatus> {
        const response = await api.get<AuthStatus>('/auth/status');
        return response.data;
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem('token');
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    getUsername(): string | null {
        return localStorage.getItem('username');
    }

    initAuth(): void {
        const token = this.getToken();
        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }
    }
}

export default new authService();