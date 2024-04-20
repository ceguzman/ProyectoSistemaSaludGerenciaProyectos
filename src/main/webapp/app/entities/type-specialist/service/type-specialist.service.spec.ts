import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeSpecialist } from '../type-specialist.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-specialist.test-samples';

import { TypeSpecialistService } from './type-specialist.service';

const requireRestSample: ITypeSpecialist = {
  ...sampleWithRequiredData,
};

describe('TypeSpecialist Service', () => {
  let service: TypeSpecialistService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeSpecialist | ITypeSpecialist[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeSpecialistService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TypeSpecialist', () => {
      const typeSpecialist = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeSpecialist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeSpecialist', () => {
      const typeSpecialist = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeSpecialist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeSpecialist', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeSpecialist', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeSpecialist', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeSpecialistToCollectionIfMissing', () => {
      it('should add a TypeSpecialist to an empty array', () => {
        const typeSpecialist: ITypeSpecialist = sampleWithRequiredData;
        expectedResult = service.addTypeSpecialistToCollectionIfMissing([], typeSpecialist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeSpecialist);
      });

      it('should not add a TypeSpecialist to an array that contains it', () => {
        const typeSpecialist: ITypeSpecialist = sampleWithRequiredData;
        const typeSpecialistCollection: ITypeSpecialist[] = [
          {
            ...typeSpecialist,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeSpecialistToCollectionIfMissing(typeSpecialistCollection, typeSpecialist);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeSpecialist to an array that doesn't contain it", () => {
        const typeSpecialist: ITypeSpecialist = sampleWithRequiredData;
        const typeSpecialistCollection: ITypeSpecialist[] = [sampleWithPartialData];
        expectedResult = service.addTypeSpecialistToCollectionIfMissing(typeSpecialistCollection, typeSpecialist);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeSpecialist);
      });

      it('should add only unique TypeSpecialist to an array', () => {
        const typeSpecialistArray: ITypeSpecialist[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeSpecialistCollection: ITypeSpecialist[] = [sampleWithRequiredData];
        expectedResult = service.addTypeSpecialistToCollectionIfMissing(typeSpecialistCollection, ...typeSpecialistArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeSpecialist: ITypeSpecialist = sampleWithRequiredData;
        const typeSpecialist2: ITypeSpecialist = sampleWithPartialData;
        expectedResult = service.addTypeSpecialistToCollectionIfMissing([], typeSpecialist, typeSpecialist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeSpecialist);
        expect(expectedResult).toContain(typeSpecialist2);
      });

      it('should accept null and undefined values', () => {
        const typeSpecialist: ITypeSpecialist = sampleWithRequiredData;
        expectedResult = service.addTypeSpecialistToCollectionIfMissing([], null, typeSpecialist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeSpecialist);
      });

      it('should return initial array if no TypeSpecialist is added', () => {
        const typeSpecialistCollection: ITypeSpecialist[] = [sampleWithRequiredData];
        expectedResult = service.addTypeSpecialistToCollectionIfMissing(typeSpecialistCollection, undefined, null);
        expect(expectedResult).toEqual(typeSpecialistCollection);
      });
    });

    describe('compareTypeSpecialist', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeSpecialist(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeSpecialist(entity1, entity2);
        const compareResult2 = service.compareTypeSpecialist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeSpecialist(entity1, entity2);
        const compareResult2 = service.compareTypeSpecialist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeSpecialist(entity1, entity2);
        const compareResult2 = service.compareTypeSpecialist(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
