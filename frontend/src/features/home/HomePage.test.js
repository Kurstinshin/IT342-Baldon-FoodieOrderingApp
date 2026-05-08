/**
 * HomePage.test.js — Home Page Tests
 */
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter } from 'react-router-dom';
import Home from './HomePage';

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

const renderHome = () => render(<BrowserRouter><Home /></BrowserRouter>);

beforeEach(() => { jest.clearAllMocks(); localStorage.clear(); });

describe('HomePage Component', () => {
  test('renders the hero section with heading', () => {
    renderHome();
    expect(screen.getByText(/Happy With/i)).toBeInTheDocument();
    expect(screen.getByText(/Delicious/i)).toBeInTheDocument();
  });

  test('renders the hero description text', () => {
    renderHome();
    expect(screen.getByText(/Exploring new food/i)).toBeInTheDocument();
  });

  test('renders Order Food button', () => {
    renderHome();
    expect(screen.getByText(/Order Food/i)).toBeInTheDocument();
  });

  test('renders Learn More button', () => {
    renderHome();
    expect(screen.getByText(/Learn More/i)).toBeInTheDocument();
  });

  test('navigates to /dashboard on Order Food click', () => {
    renderHome();
    fireEvent.click(screen.getByText(/Order Food/i));
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
  });

  test('renders How You Can Order section', () => {
    renderHome();
    expect(screen.getByText(/How You Can Order/i)).toBeInTheDocument();
  });

  test('renders the hero image', () => {
    renderHome();
    const img = screen.getByAltText('food');
    expect(img).toBeInTheDocument();
    expect(img).toHaveAttribute('src', 'homeimage.jpeg');
  });
});
