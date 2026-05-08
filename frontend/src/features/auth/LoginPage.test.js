/**
 * LoginPage.test.js — Login Feature Tests
 *
 * Tests the Login component rendering, form validation,
 * successful login flow, and error handling.
 */
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router-dom';
import Login from './LoginPage';

// Mock the API module
jest.mock('../../shared/api/api', () => ({
  __esModule: true,
  default: {
    post: jest.fn(),
    get: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
    interceptors: { request: { use: jest.fn() } },
  },
}));

// Mock useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

import API from '../../shared/api/api';

const renderLogin = () =>
  render(
    <BrowserRouter>
      <Login />
    </BrowserRouter>
  );

beforeEach(() => {
  jest.clearAllMocks();
  localStorage.clear();
});

describe('LoginPage Component', () => {

  test('renders the login form with email and password fields', () => {
    renderLogin();
    expect(screen.getByPlaceholderText('Email address')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
  });

  test('renders the app name "FoodieApp"', () => {
    renderLogin();
    expect(screen.getByText(/FoodieApp/)).toBeInTheDocument();
  });

  test('renders the Sign In tab as active', () => {
    renderLogin();
    const signInTab = screen.getByText('Sign In');
    expect(signInTab).toHaveClass('active');
  });

  test('renders the Login button', () => {
    renderLogin();
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();
  });

  test('renders the "Forgot password?" link', () => {
    renderLogin();
    expect(screen.getByText('Forgot password?')).toBeInTheDocument();
  });

  test('renders social login icons (google, facebook, twitter)', () => {
    renderLogin();
    expect(screen.getByAltText('google')).toBeInTheDocument();
    expect(screen.getByAltText('facebook')).toBeInTheDocument();
    expect(screen.getByAltText('twitter')).toBeInTheDocument();
  });

  test('allows typing into email and password fields', async () => {
    renderLogin();
    const emailInput = screen.getByPlaceholderText('Email address');
    const passwordInput = screen.getByPlaceholderText('Password');

    await userEvent.type(emailInput, 'test@example.com');
    await userEvent.type(passwordInput, 'Password123');

    expect(emailInput).toHaveValue('test@example.com');
    expect(passwordInput).toHaveValue('Password123');
  });

  test('calls API and navigates to /dashboard on successful CUSTOMER login', async () => {
    API.post.mockResolvedValueOnce({
      data: {
        data: {
          token: 'jwt-token-123',
          userId: 1,
          username: 'John Doe',
          role: 'CUSTOMER',
        },
      },
    });

    renderLogin();
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'john@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'pass123');
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    await waitFor(() => {
      expect(API.post).toHaveBeenCalledWith('/auth/login', {
        email: 'john@test.com',
        password: 'pass123',
      });
    });

    await waitFor(() => {
      expect(localStorage.getItem('token')).toBe('jwt-token-123');
      expect(localStorage.getItem('userId')).toBe('1');
      expect(localStorage.getItem('userName')).toBe('John Doe');
      expect(localStorage.getItem('userRole')).toBe('CUSTOMER');
      expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
    });
  });

  test('navigates to /admin on successful ADMIN login', async () => {
    API.post.mockResolvedValueOnce({
      data: {
        data: {
          token: 'admin-token-456',
          userId: 2,
          username: 'Admin User',
          role: 'ADMIN',
        },
      },
    });

    renderLogin();
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'admin@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'admin123');
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/admin');
    });
  });

  test('shows alert on login failure', async () => {
    const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});
    API.post.mockRejectedValueOnce({
      response: {
        data: {
          error: { message: 'Invalid credentials' },
        },
      },
    });

    renderLogin();
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'bad@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'wrongpass');
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalledWith('Invalid credentials');
    });

    alertMock.mockRestore();
  });

  test('shows "Logging in..." while loading', async () => {
    // Make the API call hang
    API.post.mockImplementation(() => new Promise(() => {}));

    renderLogin();
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'test@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'pass');
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /logging in/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /logging in/i })).toBeDisabled();
    });
  });

  test('navigates to /register when Sign Up tab is clicked', () => {
    renderLogin();
    fireEvent.click(screen.getByText('Sign Up'));
    expect(mockNavigate).toHaveBeenCalledWith('/register');
  });
});
