jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TypeSpecialistService } from '../service/type-specialist.service';

import { TypeSpecialistDeleteDialogComponent } from './type-specialist-delete-dialog.component';

describe('TypeSpecialist Management Delete Component', () => {
  let comp: TypeSpecialistDeleteDialogComponent;
  let fixture: ComponentFixture<TypeSpecialistDeleteDialogComponent>;
  let service: TypeSpecialistService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TypeSpecialistDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TypeSpecialistDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeSpecialistDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeSpecialistService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
