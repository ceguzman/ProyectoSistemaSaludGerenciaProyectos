import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMedicalProcedures } from '../medical-procedures.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../medical-procedures.test-samples';

import { MedicalProceduresService, RestMedicalProcedures } from './medical-procedures.service';

const requireRestSample: RestMedicalProcedures = {
  ...sampleWithRequiredData,
  dateProcedures: sampleWithRequiredData.dateProcedures?.format(DATE_FORMAT),
};

describe('MedicalProcedures Service', () => {
  let service: MedicalProceduresService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicalProcedures | IMedicalProcedures[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MedicalProceduresService);
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

    it('should create a MedicalProcedures', () => {
      const medicalProcedures = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicalProcedures).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedicalProcedures', () => {
      const medicalProcedures = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicalProcedures).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedicalProcedures', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedicalProcedures', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MedicalProcedures', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicalProceduresToCollectionIfMissing', () => {
      it('should add a MedicalProcedures to an empty array', () => {
        const medicalProcedures: IMedicalProcedures = sampleWithRequiredData;
        expectedResult = service.addMedicalProceduresToCollectionIfMissing([], medicalProcedures);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalProcedures);
      });

      it('should not add a MedicalProcedures to an array that contains it', () => {
        const medicalProcedures: IMedicalProcedures = sampleWithRequiredData;
        const medicalProceduresCollection: IMedicalProcedures[] = [
          {
            ...medicalProcedures,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicalProceduresToCollectionIfMissing(medicalProceduresCollection, medicalProcedures);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedicalProcedures to an array that doesn't contain it", () => {
        const medicalProcedures: IMedicalProcedures = sampleWithRequiredData;
        const medicalProceduresCollection: IMedicalProcedures[] = [sampleWithPartialData];
        expectedResult = service.addMedicalProceduresToCollectionIfMissing(medicalProceduresCollection, medicalProcedures);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalProcedures);
      });

      it('should add only unique MedicalProcedures to an array', () => {
        const medicalProceduresArray: IMedicalProcedures[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicalProceduresCollection: IMedicalProcedures[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalProceduresToCollectionIfMissing(medicalProceduresCollection, ...medicalProceduresArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicalProcedures: IMedicalProcedures = sampleWithRequiredData;
        const medicalProcedures2: IMedicalProcedures = sampleWithPartialData;
        expectedResult = service.addMedicalProceduresToCollectionIfMissing([], medicalProcedures, medicalProcedures2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalProcedures);
        expect(expectedResult).toContain(medicalProcedures2);
      });

      it('should accept null and undefined values', () => {
        const medicalProcedures: IMedicalProcedures = sampleWithRequiredData;
        expectedResult = service.addMedicalProceduresToCollectionIfMissing([], null, medicalProcedures, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalProcedures);
      });

      it('should return initial array if no MedicalProcedures is added', () => {
        const medicalProceduresCollection: IMedicalProcedures[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalProceduresToCollectionIfMissing(medicalProceduresCollection, undefined, null);
        expect(expectedResult).toEqual(medicalProceduresCollection);
      });
    });

    describe('compareMedicalProcedures', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicalProcedures(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicalProcedures(entity1, entity2);
        const compareResult2 = service.compareMedicalProcedures(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicalProcedures(entity1, entity2);
        const compareResult2 = service.compareMedicalProcedures(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicalProcedures(entity1, entity2);
        const compareResult2 = service.compareMedicalProcedures(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
