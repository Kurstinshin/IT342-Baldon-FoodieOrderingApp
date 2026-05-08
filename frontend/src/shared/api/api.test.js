/**
 * api.test.js — Shared API Service Tests
 */
import axios from 'axios';

const mockInterceptors = { request: { use: jest.fn() }, response: { use: jest.fn() } };
const mockInstance = { interceptors: mockInterceptors, get: jest.fn(), post: jest.fn() };

jest.mock('axios', () => {
  return {
    create: jest.fn(),
  };
});

describe('API Service', () => {
  beforeEach(() => {
    axios.create.mockReturnValue(mockInstance);
  });

  test('creates axios instance with correct baseURL', () => {
    jest.isolateModules(() => {
      require('./api');
      expect(axios.create).toHaveBeenCalledWith(
        expect.objectContaining({
          baseURL: expect.stringContaining('localhost:8080/api/v1'),
        })
      );
    });
  });

  test('registers a request interceptor', () => {
    jest.isolateModules(() => {
      const API = require('./api').default;
      expect(mockInterceptors.request.use).toHaveBeenCalled();
    });
  });
});

