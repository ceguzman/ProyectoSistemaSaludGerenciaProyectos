import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMedicalAuthorization } from '../medical-authorization.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../medical-authorization.test-samples';

import { MedicalAuthorizationService, RestMedicalAuthorization } from './medical-authorization.service';

const requireRestSample: RestMedicalAuthorization = {
  ...sampleWithRequiredData,
  dateAuthorization: sampleWithRequiredData.dateAuthorization?.format(DATE_FORMAT),
};

describe('MedicalAuthorization Service', () => {
  let service: MedicalAuthorizationService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicalAuthorization | IMedicalAuthorization[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MedicalAuthorizationService);
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

    it('should create a MedicalAuthorization', () => {
      const medicalAuthorization = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicalAuthorization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedicalAuthorization', () => {
      const medicalAuthorization = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicalAuthorization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedicalAuthorization', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedicalAuthorization', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MedicalAuthorization', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicalAuthorizationToCollectionIfMissing', () => {
      it('should add a MedicalAuthorization to an empty array', () => {
        const medicalAuthorization: IMedicalAuthorization = sampleWithRequiredData;
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing([], medicalAuthorization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalAuthorization);
      });

      it('should not add a MedicalAuthorization to an array that contains it', () => {
        const medicalAuthorization: IMedicalAuthorization = sampleWithRequiredData;
        const medicalAuthorizationCollection: IMedicalAuthorization[] = [
          {
            ...medicalAuthorization,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing(medicalAuthorizationCollection, medicalAuthorization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedicalAuthorization to an array that doesn't contain it", () => {
        const medicalAuthorization: IMedicalAuthorization = sampleWithRequiredData;
        const medicalAuthorizationCollection: IMedicalAuthorization[] = [sampleWithPartialData];
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing(medicalAuthorizationCollection, medicalAuthorization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalAuthorization);
      });

      it('should add only unique MedicalAuthorization to an array', () => {
        const medicalAuthorizationArray: IMedicalAuthorization[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicalAuthorizationCollection: IMedicalAuthorization[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing(medicalAuthorizationCollection, ...medicalAuthorizationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicalAuthorization: IMedicalAuthorization = sampleWithRequiredData;
        const medicalAuthorization2: IMedicalAuthorization = sampleWithPartialData;
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing([], medicalAuthorization, medicalAuthorization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalAuthorization);
        expect(expectedResult).toContain(medicalAuthorization2);
      });

      it('should accept null and undefined values', () => {
        const medicalAuthorization: IMedicalAuthorization = sampleWithRequiredData;
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing([], null, medicalAuthorization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalAuthorization);
      });

      it('should return initial array if no MedicalAuthorization is added', () => {
        const medicalAuthorizationCollection: IMedicalAuthorization[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalAuthorizationToCollectionIfMissing(medicalAuthorizationCollection, undefined, null);
        expect(expectedResult).toEqual(medicalAuthorizationCollection);
      });
    });

    describe('compareMedicalAuthorization', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicalAuthorization(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicalAuthorization(entity1, entity2);
        const compareResult2 = service.compareMedicalAuthorization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicalAuthorization(entity1, entity2);
        const compareResult2 = service.compareMedicalAuthorization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicalAuthorization(entity1, entity2);
        const compareResult2 = service.compareMedicalAuthorization(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
