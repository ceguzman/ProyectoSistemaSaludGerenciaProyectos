import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMedicalAppointments } from '../medical-appointments.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../medical-appointments.test-samples';

import { MedicalAppointmentsService, RestMedicalAppointments } from './medical-appointments.service';

const requireRestSample: RestMedicalAppointments = {
  ...sampleWithRequiredData,
  dateMedical: sampleWithRequiredData.dateMedical?.format(DATE_FORMAT),
};

describe('MedicalAppointments Service', () => {
  let service: MedicalAppointmentsService;
  let httpMock: HttpTestingController;
  let expectedResult: IMedicalAppointments | IMedicalAppointments[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MedicalAppointmentsService);
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

    it('should create a MedicalAppointments', () => {
      const medicalAppointments = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(medicalAppointments).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MedicalAppointments', () => {
      const medicalAppointments = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(medicalAppointments).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MedicalAppointments', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MedicalAppointments', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MedicalAppointments', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMedicalAppointmentsToCollectionIfMissing', () => {
      it('should add a MedicalAppointments to an empty array', () => {
        const medicalAppointments: IMedicalAppointments = sampleWithRequiredData;
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing([], medicalAppointments);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalAppointments);
      });

      it('should not add a MedicalAppointments to an array that contains it', () => {
        const medicalAppointments: IMedicalAppointments = sampleWithRequiredData;
        const medicalAppointmentsCollection: IMedicalAppointments[] = [
          {
            ...medicalAppointments,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing(medicalAppointmentsCollection, medicalAppointments);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MedicalAppointments to an array that doesn't contain it", () => {
        const medicalAppointments: IMedicalAppointments = sampleWithRequiredData;
        const medicalAppointmentsCollection: IMedicalAppointments[] = [sampleWithPartialData];
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing(medicalAppointmentsCollection, medicalAppointments);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalAppointments);
      });

      it('should add only unique MedicalAppointments to an array', () => {
        const medicalAppointmentsArray: IMedicalAppointments[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const medicalAppointmentsCollection: IMedicalAppointments[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing(medicalAppointmentsCollection, ...medicalAppointmentsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const medicalAppointments: IMedicalAppointments = sampleWithRequiredData;
        const medicalAppointments2: IMedicalAppointments = sampleWithPartialData;
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing([], medicalAppointments, medicalAppointments2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(medicalAppointments);
        expect(expectedResult).toContain(medicalAppointments2);
      });

      it('should accept null and undefined values', () => {
        const medicalAppointments: IMedicalAppointments = sampleWithRequiredData;
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing([], null, medicalAppointments, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(medicalAppointments);
      });

      it('should return initial array if no MedicalAppointments is added', () => {
        const medicalAppointmentsCollection: IMedicalAppointments[] = [sampleWithRequiredData];
        expectedResult = service.addMedicalAppointmentsToCollectionIfMissing(medicalAppointmentsCollection, undefined, null);
        expect(expectedResult).toEqual(medicalAppointmentsCollection);
      });
    });

    describe('compareMedicalAppointments', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMedicalAppointments(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMedicalAppointments(entity1, entity2);
        const compareResult2 = service.compareMedicalAppointments(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMedicalAppointments(entity1, entity2);
        const compareResult2 = service.compareMedicalAppointments(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMedicalAppointments(entity1, entity2);
        const compareResult2 = service.compareMedicalAppointments(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
