import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMedicalAuthorization } from 'app/entities/medical-authorization/medical-authorization.model';
import { MedicalAuthorizationService } from 'app/entities/medical-authorization/service/medical-authorization.service';
import { MedicationRequestService } from '../service/medication-request.service';
import { IMedicationRequest } from '../medication-request.model';
import { MedicationRequestFormService } from './medication-request-form.service';

import { MedicationRequestUpdateComponent } from './medication-request-update.component';

describe('MedicationRequest Management Update Component', () => {
  let comp: MedicationRequestUpdateComponent;
  let fixture: ComponentFixture<MedicationRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let medicationRequestFormService: MedicationRequestFormService;
  let medicationRequestService: MedicationRequestService;
  let medicalAuthorizationService: MedicalAuthorizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MedicationRequestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MedicationRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MedicationRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    medicationRequestFormService = TestBed.inject(MedicationRequestFormService);
    medicationRequestService = TestBed.inject(MedicationRequestService);
    medicalAuthorizationService = TestBed.inject(MedicalAuthorizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MedicalAuthorization query and add missing value', () => {
      const medicationRequest: IMedicationRequest = { id: 456 };
      const medicalAuthorization: IMedicalAuthorization = { id: 19423 };
      medicationRequest.medicalAuthorization = medicalAuthorization;

      const medicalAuthorizationCollection: IMedicalAuthorization[] = [{ id: 5075 }];
      jest.spyOn(medicalAuthorizationService, 'query').mockReturnValue(of(new HttpResponse({ body: medicalAuthorizationCollection })));
      const additionalMedicalAuthorizations = [medicalAuthorization];
      const expectedCollection: IMedicalAuthorization[] = [...additionalMedicalAuthorizations, ...medicalAuthorizationCollection];
      jest.spyOn(medicalAuthorizationService, 'addMedicalAuthorizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ medicationRequest });
      comp.ngOnInit();

      expect(medicalAuthorizationService.query).toHaveBeenCalled();
      expect(medicalAuthorizationService.addMedicalAuthorizationToCollectionIfMissing).toHaveBeenCalledWith(
        medicalAuthorizationCollection,
        ...additionalMedicalAuthorizations.map(expect.objectContaining),
      );
      expect(comp.medicalAuthorizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const medicationRequest: IMedicationRequest = { id: 456 };
      const medicalAuthorization: IMedicalAuthorization = { id: 29985 };
      medicationRequest.medicalAuthorization = medicalAuthorization;

      activatedRoute.data = of({ medicationRequest });
      comp.ngOnInit();

      expect(comp.medicalAuthorizationsSharedCollection).toContain(medicalAuthorization);
      expect(comp.medicationRequest).toEqual(medicationRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicationRequest>>();
      const medicationRequest = { id: 123 };
      jest.spyOn(medicationRequestFormService, 'getMedicationRequest').mockReturnValue(medicationRequest);
      jest.spyOn(medicationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicationRequest }));
      saveSubject.complete();

      // THEN
      expect(medicationRequestFormService.getMedicationRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(medicationRequestService.update).toHaveBeenCalledWith(expect.objectContaining(medicationRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicationRequest>>();
      const medicationRequest = { id: 123 };
      jest.spyOn(medicationRequestFormService, 'getMedicationRequest').mockReturnValue({ id: null });
      jest.spyOn(medicationRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicationRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: medicationRequest }));
      saveSubject.complete();

      // THEN
      expect(medicationRequestFormService.getMedicationRequest).toHaveBeenCalled();
      expect(medicationRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedicationRequest>>();
      const medicationRequest = { id: 123 };
      jest.spyOn(medicationRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ medicationRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(medicationRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedicalAuthorization', () => {
      it('Should forward to medicalAuthorizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medicalAuthorizationService, 'compareMedicalAuthorization');
        comp.compareMedicalAuthorization(entity, entity2);
        expect(medicalAuthorizationService.compareMedicalAuthorization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
