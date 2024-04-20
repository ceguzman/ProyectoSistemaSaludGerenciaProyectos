import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeDiseases } from '../type-diseases.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-diseases.test-samples';

import { TypeDiseasesService } from './type-diseases.service';

const requireRestSample: ITypeDiseases = {
  ...sampleWithRequiredData,
};

describe('TypeDiseases Service', () => {
  let service: TypeDiseasesService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeDiseases | ITypeDiseases[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeDiseasesService);
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

    it('should create a TypeDiseases', () => {
      const typeDiseases = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeDiseases).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeDiseases', () => {
      const typeDiseases = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeDiseases).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeDiseases', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeDiseases', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeDiseases', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeDiseasesToCollectionIfMissing', () => {
      it('should add a TypeDiseases to an empty array', () => {
        const typeDiseases: ITypeDiseases = sampleWithRequiredData;
        expectedResult = service.addTypeDiseasesToCollectionIfMissing([], typeDiseases);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeDiseases);
      });

      it('should not add a TypeDiseases to an array that contains it', () => {
        const typeDiseases: ITypeDiseases = sampleWithRequiredData;
        const typeDiseasesCollection: ITypeDiseases[] = [
          {
            ...typeDiseases,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeDiseasesToCollectionIfMissing(typeDiseasesCollection, typeDiseases);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeDiseases to an array that doesn't contain it", () => {
        const typeDiseases: ITypeDiseases = sampleWithRequiredData;
        const typeDiseasesCollection: ITypeDiseases[] = [sampleWithPartialData];
        expectedResult = service.addTypeDiseasesToCollectionIfMissing(typeDiseasesCollection, typeDiseases);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeDiseases);
      });

      it('should add only unique TypeDiseases to an array', () => {
        const typeDiseasesArray: ITypeDiseases[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeDiseasesCollection: ITypeDiseases[] = [sampleWithRequiredData];
        expectedResult = service.addTypeDiseasesToCollectionIfMissing(typeDiseasesCollection, ...typeDiseasesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeDiseases: ITypeDiseases = sampleWithRequiredData;
        const typeDiseases2: ITypeDiseases = sampleWithPartialData;
        expectedResult = service.addTypeDiseasesToCollectionIfMissing([], typeDiseases, typeDiseases2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeDiseases);
        expect(expectedResult).toContain(typeDiseases2);
      });

      it('should accept null and undefined values', () => {
        const typeDiseases: ITypeDiseases = sampleWithRequiredData;
        expectedResult = service.addTypeDiseasesToCollectionIfMissing([], null, typeDiseases, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeDiseases);
      });

      it('should return initial array if no TypeDiseases is added', () => {
        const typeDiseasesCollection: ITypeDiseases[] = [sampleWithRequiredData];
        expectedResult = service.addTypeDiseasesToCollectionIfMissing(typeDiseasesCollection, undefined, null);
        expect(expectedResult).toEqual(typeDiseasesCollection);
      });
    });

    describe('compareTypeDiseases', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeDiseases(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeDiseases(entity1, entity2);
        const compareResult2 = service.compareTypeDiseases(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeDiseases(entity1, entity2);
        const compareResult2 = service.compareTypeDiseases(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeDiseases(entity1, entity2);
        const compareResult2 = service.compareTypeDiseases(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
