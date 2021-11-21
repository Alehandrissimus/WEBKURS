import { TestBed } from '@angular/core/testing';

import { AbstractCrudService } from './abstract-crud.service';

describe('AbstractCrudService', () => {
  let service: AbstractCrudService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AbstractCrudService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
