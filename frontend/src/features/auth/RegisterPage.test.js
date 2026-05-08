/**
 * RegisterPage.test.js — Registration Feature Tests
 *
 * Tests the Register component rendering, form fields,
 * successful registration flow, and error handling.
 */
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router-dom';
import Register from './RegisterPage';

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

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

import API from '../../shared/api/api';

const renderRegister = () =>
  render(
    <BrowserRouter>
      <Register />
    </BrowserRouter>
  );

beforeEach(() => {
  jest.clearAllMocks();
  localStorage.clear();
});

describe('RegisterPage Component', () => {

  test('renders the registration form with all required fields', () => {
    renderRegister();
    expect(screen.getByPlaceholderText('First Name')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Last Name')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Email address')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
  });

  test('renders the app name "FoodieApp"', () => {
    renderRegister();
    expect(screen.getByText(/FoodieApp/)).toBeInTheDocument();
  });

  test('renders the Sign Up tab as active', () => {
    renderRegister();
    const signUpTab = screen.getByText('Sign Up');
    expect(signUpTab).toHaveClass('active');
  });

  test('renders the Create Account button', () => {
    renderRegister();
    expect(screen.getByRole('button', { name: /create account/i })).toBeInTheDocument();
  });

  test('renders social login icons', () => {
    renderRegister();
    expect(screen.getByAltText('google')).toBeInTheDocument();
    expect(screen.getByAltText('facebook')).toBeInTheDocument();
    expect(screen.getByAltText('twitter')).toBeInTheDocument();
  });

  test('allows typing into all form fields', async () => {
    renderRegister();
    const firstNameInput = screen.getByPlaceholderText('First Name');
    const lastNameInput = screen.getByPlaceholderText('Last Name');
    const emailInput = screen.getByPlaceholderText('Email address');
    const passwordInput = screen.getByPlaceholderText('Password');

    await userEvent.type(firstNameInput, 'John');
    await userEvent.type(lastNameInput, 'Doe');
    await userEvent.type(emailInput, 'john@test.com');
    await userEvent.type(passwordInput, 'SecurePass123');

    expect(firstNameInput).toHaveValue('John');
    expect(lastNameInput).toHaveValue('Doe');
    expect(emailInput).toHaveValue('john@test.com');
    expect(passwordInput).toHaveValue('SecurePass123');
  });

  test('calls API and navigates to /login on successful registration', async () => {
    const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});
    API.post.mockResolvedValueOnce({
      data: { message: 'User registered successfully' },
    });

    renderRegister();
    await userEvent.type(screen.getByPlaceholderText('First Name'), 'Jane');
    await userEvent.type(screen.getByPlaceholderText('Last Name'), 'Smith');
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'jane@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'pass123');
    fireEvent.click(screen.getByRole('button', { name: /create account/i }));

    await waitFor(() => {
      expect(API.post).toHaveBeenCalledWith('/auth/register', {
        firstname: 'Jane',
        lastname: 'Smith',
        email: 'jane@test.com',
        password: 'pass123',
      });
    });

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalledWith('User registered successfully');
      expect(mockNavigate).toHaveBeenCalledWith('/login');
    });

    alertMock.mockRestore();
  });

  test('shows alert on registration failure', async () => {
    const alertMock = jest.spyOn(window, 'alert').mockImplementation(() => {});
    API.post.mockRejectedValueOnce({
      response: {
        data: {
          error: { message: 'Email already registered' },
        },
      },
    });

    renderRegister();
    await userEvent.type(screen.getByPlaceholderText('First Name'), 'John');
    await userEvent.type(screen.getByPlaceholderText('Last Name'), 'Doe');
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'existing@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'pass');
    fireEvent.click(screen.getByRole('button', { name: /create account/i }));

    await waitFor(() => {
      expect(alertMock).toHaveBeenCalledWith('Email already registered');
    });

    alertMock.mockRestore();
  });

  test('shows "Creating account..." while loading', async () => {
    API.post.mockImplementation(() => new Promise(() => {}));

    renderRegister();
    await userEvent.type(screen.getByPlaceholderText('First Name'), 'Test');
    await userEvent.type(screen.getByPlaceholderText('Last Name'), 'User');
    await userEvent.type(screen.getByPlaceholderText('Email address'), 'test@test.com');
    await userEvent.type(screen.getByPlaceholderText('Password'), 'pass');
    fireEvent.click(screen.getByRole('button', { name: /create account/i }));

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /creating account/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /creating account/i })).toBeDisabled();
    });
  });

  test('navigates to /login when Sign In tab is clicked', () => {
    renderRegister();
    fireEvent.click(screen.getByText('Sign In'));
    expect(mockNavigate).toHaveBeenCalledWith('/login');
  });
});
